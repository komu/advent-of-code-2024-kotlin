import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds

fun <T> Collection<T>.progressIterator(step: Int? = null): Iterator<T> = iterator().withProgress(size, step)

fun <T> Iterator<T>.withProgress(max: Int, step: Int? = null): Iterator<T> = iterator {
    val start = Instant.now()
    val effectiveStep = step ?: 1000

    for ((i, v) in this@withProgress.iterator().withIndex()) {
        if (i % effectiveStep == 0) {
            val elapsed = Instant.now().toEpochMilli() - start.toEpochMilli()
            val ratio = i/max.toDouble()
            val total = elapsed / ratio
            val remaining = if (total.isNaN()) "-" else (total - elapsed).milliseconds.toString()

            print("\r$i/$max ($remaining remaining)")
            System.out.flush()
        }
        yield(v)
    }
    print("\r                             \r")
    System.out.flush()
}
