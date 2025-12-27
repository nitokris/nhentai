package com.nitokrisalpha.business.builder

import com.nitokrisalpha.business.entity.WorkMetadata

class WorkMetadataBuilder {
    var title: String = ""
    var cover: String = ""
    var format: String = ""
    var series: String = ""
    var summary: String = ""
    private val _tags: MutableSet<String> = linkedSetOf()
    private val _images: MutableSet<String> = linkedSetOf()
    var subjectMatter: String = ""

    fun title(value: String) = apply { this.title = value }
    fun cover(value: String) = apply { this.cover = value }
    fun format(value: String) = apply { this.format = value }
    fun series(value: String) = apply { this.series = value }
    fun summary(value: String) = apply { this.summary = value }
    fun subjectMatter(value: String) = apply { this.subjectMatter = value }

    fun addTag(tag: String) = apply { _tags.add(tag) }
    fun addTags(vararg tags: String) = apply { _tags.addAll(tags) }
    fun addTags(tags: Iterable<String>) = apply { _tags.addAll(tags) }
    fun setTags(tags: Iterable<String>) = apply {
        _tags.clear()
        _tags.addAll(tags)
    }

    fun addImage(image: String) = apply { _images.add(image) }
    fun addImages(vararg images: String) = apply { _images.addAll(images) }
    fun addImages(images: Iterable<String>) = apply { _images.addAll(images) }
    fun setImages(images: Iterable<String>) = apply {
        _images.clear()
        _images.addAll(images)
    }

    fun build(): WorkMetadata =
        WorkMetadata(
            title = title,
            cover = cover,
            format = format,
            series = series,
            summary = summary,
            tags = _tags.toSet(),
            images = _images.toSet(),
            subjectMatter = subjectMatter
        )

    companion object {
        fun from(orig: WorkMetadata): WorkMetadataBuilder = WorkMetadataBuilder().apply {
            title = orig.title
            cover = orig.cover
            format = orig.format
            series = orig.series
            summary = orig.summary
            subjectMatter = orig.subjectMatter
            _tags.clear()
            _tags.addAll(orig.tags)
            _images.clear()
            _images.addAll(orig.images)
        }
    }
}

fun WorkMetadata.toBuilder(): WorkMetadataBuilder = WorkMetadataBuilder.from(this)

fun buildMetadata(block: WorkMetadataBuilder.() -> Unit): WorkMetadata {
    return WorkMetadataBuilder().apply(block).build()
}