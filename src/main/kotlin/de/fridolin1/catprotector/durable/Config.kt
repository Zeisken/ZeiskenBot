package de.fridolin1.catprotector.durable

class Config(val prefix: String, val suffix: String) {
    companion object {
        @JvmStatic
        lateinit var config: Config
    }
}