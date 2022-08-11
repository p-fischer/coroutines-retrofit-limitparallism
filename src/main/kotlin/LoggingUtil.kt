import java.time.Clock
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val localTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

fun currentTime(): String =
    localTimeFormat.format(Clock.systemUTC().instant().atZone(ZoneId.systemDefault()).toLocalTime())

fun currentThreadInfo() = Thread.currentThread().info()

private fun Thread.info(): String = "Thread[id=$id, name=$name]"
