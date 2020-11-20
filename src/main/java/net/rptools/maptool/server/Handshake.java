/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.server;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Player;

/** @author trevor */
public class Handshake {

  public interface Code {
    public static final int UNKNOWN = 0;
    public static final int OK = 1;
    public static final int ERROR = 2;
  }

  /**
   * Server side of the handshake
   *
   * @param server the MapTool server instance
   * @param s the server socket
   * @throws IOException if an I/O error occurs when creating the input stream, the socket is
   *     closed, the socket is not connected, or the socket input has been shutdown using
   * @return A player structure for the connected player or null on issues
   */
  public static Player receiveHandshake(MapToolServer server, Socket s) throws IOException {

    Response response = new Response();
    Request request = decodeRequest(s.getInputStream());
    if (request == null) {
      response.code = Code.ERROR;
      response.message = I18N.getString("Handshake.msg.wrongPassword");
    } else if (server.isPlayerConnected(request.name)) { // Enforce a unique name
      response.code = Code.ERROR;
      response.message = I18N.getString("Handshake.msg.duplicateName");
    } else if (!MapTool.isDevelopment()
        && !MapTool.getVersion().equals(request.version)
        && !"DEVELOPMENT".equals(request.version)
        && !"@buildNumber@".equals(request.version)) {
      // Allows a version running without a 'version.txt' to act as client or server to any other
      // version

      response.code = Code.ERROR;
      String clientUsed = request.version;
      String serverUsed = MapTool.getVersion();
      response.message = I18N.getText("Handshake.msg.wrongVersion", clientUsed, serverUsed);
    } else {
      response.code = Code.OK;
    }


    HessianOutput output = new HessianOutput(s.getOutputStream());
    response.policy = server.getPolicy();
    output.writeObject(response);
    return response.code == Code.OK
        ? new Player(request.name, Player.Role.valueOf(request.role), request.password)
        : null;
  }

  private static Request decodeRequest(InputStream inputStream) {

  }

  /**
   * Client side of the handshake
   *
   * @param request the handshake request
   * @param s the socket to send the request on
   * @throws IOException if an I/O error occurs when creating the input stream, the socket is
   *     closed, the socket is not connected, or the socket input has been shutdown using
   * @return the response from the srever
   */
  public static Response sendHandshake(Request request, Socket s) throws IOException {
    HessianInput input = new HessianInput(s.getInputStream());
    HessianOutput output = new HessianOutput(s.getOutputStream());
    // Jamz: Method renamed in Hessian 4.0.+
    // output.findSerializerFactory().setAllowNonSerializable(true);
    output.getSerializerFactory().setAllowNonSerializable(true);
    output.writeObject(request);

    return (Response) input.readObject();
  }

  public static class Request {
    public String name;
    public String password;
    public String role;
    public String version;

    public Request() {
      // for serialization
    }

    public Request(String name, String password, Player.Role role, String version) {
      this.name = name;
      this.password = password;
      this.role = role.name();
      this.version = version;
    }
  }

  public static class Response {
    public int code;
    public String message;
    public ServerPolicy policy;
  }
}
