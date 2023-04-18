package com.example.icmproject1.model
data class Stage(val stageName: String, val artists : Array<Artist>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stage

        if (stageName != other.stageName) return false
        if (!artists.contentEquals(other.artists)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stageName.hashCode()
        result = 31 * result + artists.contentHashCode()
        return result
    }
}

data class Artist(val name: String, val hour: String)