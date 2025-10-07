package com.nitokrisalpha.application.service

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.common.logger
import com.nitokrisalpha.common.newId
import com.nitokrisalpha.common.noSlashStr
import com.nitokrisalpha.common.sha512
import com.nitokrisalpha.domain.entity.*
import com.nitokrisalpha.domain.repository.WorkRepository
import com.nitokrisalpha.domain.service.WorkMetaDataProvider
import com.nitokrisalpha.domain.specification.Specification
import com.nitokrisalpha.infranstructure.config.PathConfig
import com.nitokrisalpha.infranstructure.jdbc.WorkQueryRepository
import com.nitokrisalpha.infranstructure.jdbc.table.Works.site
import com.nitokrisalpha.infranstructure.metadata.WorkMetadataProviderFactory
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.springframework.boot.io.ApplicationResourceLoader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.UUID
import java.util.zip.ZipInputStream

@Service
class WorkService(
    private val workRepository: WorkRepository,
    private val workMetadataProviderFactory: WorkMetadataProviderFactory,
    private val workQueryRepository: WorkQueryRepository,
    private val pathConfig: PathConfig
) {
    @Transactional(rollbackFor = [Exception::class])
    fun saveNewWork(id: String, site: Site): WorkId {
        val workMetadataProvider: WorkMetaDataProvider = workMetadataProviderFactory.getProvider(site)
            ?: throw IllegalArgumentException("No metadata provider found for site: $site")
        val metaData = workMetadataProvider.fetchMetaData(id)
        val siteInfo = SiteInfo(site, id)
        var work = workRepository.findBySiteInfo(siteInfo)
        if (work != null) {
            work.changeMetaData(metaData)
            workRepository.save(work)
            return work.id
        }
        work = Work(
            id = newId(),
            metaData = metaData,
            siteInfo = siteInfo
        )
        return workRepository.save(work)
    }

    fun saveNewWork(url: String): Unit {
        val urls = url.split("\n")
        urls.sorted().forEach { url ->
            val site = Site.fromUrl(url)
            val id = extractCid(url) ?: throw IllegalArgumentException("cant find cid")
            val workMetadataProvider: WorkMetaDataProvider = workMetadataProviderFactory.getProvider(site)
                ?: throw IllegalArgumentException("No metadata provider found for site: $site")
            val metaData = workMetadataProvider.fetchMetaData(id)
            val siteInfo = SiteInfo(site, id)
            var work = workRepository.findBySiteInfo(siteInfo)
            if (work != null) {
                work.changeMetaData(metaData)
                workRepository.save(work)
                work.id
            }
            work = Work(
                id = newId(),
                metaData = metaData,
                siteInfo = siteInfo
            )
            try {
                workRepository.save(work)
            } catch (e: Exception) {
                logger.error("swallow exception,and message is:{}", e.message)
            }
        }

    }

    fun findOne(id: String): WorkDto? {
        return workQueryRepository.findById(WorkId(id))
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addMagnets(id: String, magnets: Collection<String>) {
        val work = workRepository.findById(WorkId(id)) ?: throw IllegalArgumentException("Work with id $id not found")
        val magnets = magnets.map { MagnetId(it) }
        work.addNewMagnets(magnets)
        workRepository.save(work)
    }

    fun removeMagnets(id: String, magnets: Collection<String>) {
        val work = workRepository.findById(WorkId(id)) ?: throw IllegalArgumentException("Work with id $id not found")
        val magnets = magnets.map { MagnetId(it) }
        work.removeMagnets(magnets)
        workRepository.save(work)
    }

    fun dropWork(id: String) {
        val work = workRepository.findById(WorkId(id)) ?: throw IllegalArgumentException("Work with id $id not found")
        workRepository.delete(work)
    }

    fun recent(count: Int): Collection<WorkDto> {
        return workQueryRepository.recent(count)
    }


    private fun safeFile(path: String): File {
        val file = Paths.get(path).toFile()
        if (file.exists()) return file

        // 尝试UTF-8修正
        val fixed = File(String(path.toByteArray(Charsets.UTF_8), Charset.defaultCharset()))
        if (fixed.exists()) return fixed

        throw IllegalArgumentException("File not exists: $path")
    }

    @Transactional(rollbackFor = [Exception::class])
    fun bindFile(id: String, filePath: String, displayName: String) {
        val file = safeFile(filePath)
        val work = workRepository.findById(WorkId(id))
        if (work == null) {
            logger.error("can't find work:{}", id)
            throw IllegalArgumentException("can't find work:${id}")
        }
        val sha512 = file.sha512()
        val noSlashStr = UUID.randomUUID().noSlashStr()
        val ext = FilenameUtils.getExtension(file.name)
        val files = arrayListOf<FileEntity>()
        var counter = 1
        ZipInputStream(FileInputStream(file))
            .use { zin ->
                var entry = zin.nextEntry
                while (entry != null) {
                    if (!entry.isDirectory) {
                        val originalFileName = entry.name
                        val extension = FilenameUtils.getExtension(originalFileName)
                        val fileName = "${UUID.randomUUID().noSlashStr()}.${extension}"
                        val outFile = File(pathConfig.galleries, fileName)
                        outFile.createNewFile()
                        FileOutputStream(outFile)
                            .use { fout ->
                                IOUtils.copy(zin, fout)
                            }
                        val entity = FileEntity(fileName, originalFileName, counter++)
                        files += entity
                    }
                    entry = zin.nextEntry
                }
            }
        val workFile = WorkFile(sha512, "${noSlashStr}.${ext}", displayName, filePath, file.name, files)
        work.addFile(workFile)
        workRepository.save(work)
    }

    companion object DmmUtil {
        private val cidRegex = Regex("cid=([^&/]+)")

        /**
         * 从DMM链接中提取cid
         * @param url DMM商品链接
         * @return cid，例如 d_652148，如果不存在返回 null
         */
        fun extractCid(url: String): String? {
            return cidRegex.find(url)?.groupValues?.get(1)
        }
    }

}