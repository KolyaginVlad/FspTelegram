package data

import Dependencies.TOPIC
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.WindowStore
import java.time.Duration
import java.util.*

fun topology(): StreamsBuilder.() -> Unit {
    return fun StreamsBuilder.() {
        val stringSerde = Serdes.String()
        val textLines: KStream<String, String> =
            this.stream(TOPIC, Consumed.with(Serdes.String(), Serdes.String()))

        textLines.foreach { key, value ->
            println("$key $value")
        }

        val groupedByWord: KGroupedStream<String?, String> =
            textLines
                .flatMapValues { value: String ->
                    value.lowercase(Locale.getDefault()).split("\\W+".toRegex())
                }
                .groupBy(
                    { _, word -> word },
                    Grouped.with(stringSerde, stringSerde)
                )

        groupedByWord.count(
            Materialized.`as`<String, Long, KeyValueStore<Bytes, ByteArray>>("word-count")
                .withValueSerde(Serdes.Long())
        )

        groupedByWord.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
            .count(
                Materialized.`as`<String, Long, WindowStore<Bytes, ByteArray>>("windowed-word-count")
                    .withValueSerde(Serdes.Long())
            )
    }
}