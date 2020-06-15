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
package net.rptools.maptool.model.notebook.entry;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.rptools.lib.MD5Key;
import net.rptools.maptool.model.Asset;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.notebook.NoteBook;

/**
 * Interface implemented by entries stored in a {@link NoteBook}.
 *
 * <p>All implementations of this interface are required to be immutable.
 */
public interface NoteBookEntry {
  /**
   * Returns a new id for a {@code NoteBookEntry}.
   *
   * @return a new id for a {@code NoteBookEntry}.
   */
  static UUID generateId() {
    return UUID.randomUUID();
  }

  /**
   * Returns the id of the {@code NoteBookEntry}. The id for the {@code NoteBookEntry} must not
   * change.
   *
   * @return the id of the {@code NoteBookEntry}.
   */
  UUID getId();

  /**
   * Returns the name of the {@code NoteBookEntry}.
   *
   * @return the name pf tje {@code NoteBookEntry}.
   */
  String getName();

  /**
   * Returns the id of the {@link net.rptools.maptool.model.Zone} that this {@code NoteBookEntry} is
   * on.
   *
   * @return the id of the {@link net.rptools.maptool.model.Zone} that this {@code NoteBookEntry} is
   *     on.
   */
  Optional<GUID> getZoneId();

  /**
   * Returns the path of the {@code NoteBookEntry} in the {@link NoteBook}.
   *
   * @return the path of the {@code NoteBookEntry} in the {@link NoteBook}.
   */
  String getPath();

  /**
   * Returns a new {@code NoteBookEntry} that is a copy of this one with the new name.
   *
   * @return a new {@code NoteBookEntry} that is a copy of this one with the new name.
   *     <p>If the name is the same as the current name then this method may return the original
   *     object as all implementors of this interface are immutable.
   */
  NoteBookEntry setName(String name);

  /**
   * Returns a new {@code NoteBookEntry} that is a copy of this one with the new zone id.
   *
   * <p>This method must be consistent with {@link #getZoneRequirements()}
   *
   * <ul>
   *   <li>when {@link NoteBookEntryZoneRequirements#ZONE_IGNORED} this method will ignore any
   *       input.
   *   <li>when {@link NoteBookEntryZoneRequirements#ZONE_ALLOWED} this method will allow {@code
   *       null}s
   *   <li>when {@link NoteBookEntryZoneRequirements#ZONE_REQUIRED} this method will throw {@link
   *       IllegalArgumentException} if {@code null} is passed to it.
   * </ul>
   *
   * @param id the id of the zone.
   *     <p>If the zone id is the same as the current zone id then this method may return the
   *     original object as all implementors of this interface are immutable.
   * @throws IllegalArgumentException if the {@code NoteBookEntry} requires a zone a {@code mull} is
   *     passed.
   */
  NoteBookEntry setZoneId(GUID id);

  /**
   * Returns a new {@code NoteBookEntry} that is a copy of this one with the new path.
   *
   * @return a new {@code NoteBookEntry} that is a copy of this one with the new path.
   *     <p>If the path is the same as the current path then this method may return the original
   *     object as all implementors of this interface are immutable.
   */
  NoteBookEntry setPath(String path);

  /**
   * Returns the zone requirements for this {@code NoteBookEntry}.
   *
   * @return the zone requirements for this {@code NoteBookEntry}.
   */
  NoteBookEntryZoneRequirements getZoneRequirements();

  /**
   * Returns all of the {@link MD5Key}s associated with the {@link Asset}s that the {@code
   * NoteBookEntry} contains.
   *
   * @return all of the {@link MD5Key}s associated with the {@link Asset}s.
   */
  Collection<MD5Key> getAssetKeys();

  /**
   * Returns the type of the {@code NoteBookEntry}.
   *
   * @return the type of the {@code NoteBookEntry}.
   */
  NoteBookEntryType getType();
}