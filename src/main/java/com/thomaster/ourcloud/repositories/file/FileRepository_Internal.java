package com.thomaster.ourcloud.repositories.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
interface FileRepository_Internal extends JpaRepository<PersistentFileSystemElement, Long> {

    /**SELECT * FROM persistent_file_system_element WHERE relative_path ~ lquery('Thomaster.*{0,1}')*/

    @Query(value = "SELECT * FROM persistent_file_system_element WHERE relative_path ~ lquery(:pathToSearch)", nativeQuery = true)
    Set<PersistentFileSystemElement> findAllByPath(@Param("pathToSearch") String pathToSearch);

    @Query(value = "SELECT * FROM persistent_file_system_element WHERE relative_path ~ lquery(:pathToSearch)", nativeQuery = true)
    Set<PersistentFileSystemElement> findOneByPath(@Param("pathToSearch") String pathToSearch);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM persistent_file_system_element WHERE relative_path ~ lquery(:pathToDelete)", nativeQuery = true)
    void deleteRecursivelyByPath(@Param("pathToDelete") String pathToDelete);

    @Transactional
    @Modifying
    @Query(value = "UPDATE persistent_file_system_element SET file_size = file_size + :fileSizeDelta WHERE relative_path @> ltree(:pathToUpdate)", nativeQuery = true)
    void updateFileSizeAllAncestorFolders(@Param("pathToUpdate") String pathToUpdate, @Param("fileSizeDelta") Long fileSizeDelta);

}
