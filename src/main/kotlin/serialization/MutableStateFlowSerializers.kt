package serialization

import entities.Position
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class MutableStateFlowSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableStateFlow<T>> {
    override val descriptor: SerialDescriptor = dataSerializer.descriptor
    override fun serialize(encoder: Encoder, value: MutableStateFlow<T>) = dataSerializer.serialize(encoder, value.value)
    override fun deserialize(decoder: Decoder) = MutableStateFlow(dataSerializer.deserialize(decoder))
}

object MutableStateFlowPositionSerializer : KSerializer<MutableStateFlow<Position>> by MutableStateFlowSerializer(
    Position.serializer()
)