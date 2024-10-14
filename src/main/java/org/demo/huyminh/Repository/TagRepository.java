package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 9:20 PM
 */

public interface TagRepository extends JpaRepository<Tag, String> {

    @Query(value = "SELECT * FROM tag WHERE type=:type", nativeQuery = true)
    List<Tag> findTagByType(String type);

    @Query(value = "SELECT * FROM tag WHERE name=:name AND type=:type", nativeQuery = true)
    Optional<Tag> findByIdAndType(@Param("name") String tagName, @Param("type") String type);

    boolean existsTagByDescription(String description);

    @Modifying
    @Query(value = "UPDATE tag SET name=:name, description=:description, type=:type WHERE name=:oldName", nativeQuery = true)
    void update(String name, String description, String type, String oldName);
}
