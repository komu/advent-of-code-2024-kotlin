fun digraph(name: String, block: DotBuilder.() -> Unit): String {
    val out = StringBuilder()
    out.appendLine("digraph $name {")
    block(DotBuilder(out))
    out.appendLine("}")
    return out.toString()
}

class DotBuilder(private val sb: StringBuilder) {

    fun node(id: String, vararg attributes: Pair<String, String?>) {
        node(id, attributes.toMap())
    }

    fun node(id: String, attributes: Map<String, String?> = emptyMap()) {
        sb.append("    $id")
        appendAttributes(attributes)
        sb.appendLine(";")
    }

    fun edge(from: String, to: String, attributes: Map<String, String?> = emptyMap()) {
        sb.append("    $from -> $to")
        appendAttributes(attributes)
        sb.appendLine(";")
    }

    private fun appendAttributes(attributes: Map<String, String?>) {
        if (attributes.isNotEmpty())
            attributes.entries.filter { it.value != null }.joinTo(sb, separator = " ", prefix = " [", postfix = "]") {
                "${it.key}=${encodeValue(it.value!!)}"
            }
    }

    companion object {
        // this is probably incomplete but works for our cases
        private fun encodeValue(value: String): String =
            if (value.all { it.isLetterOrDigit() })
                value
            else
                buildString {
                    append('"')
                    for (c in value)
                        when (c) {
                            '"' -> append("\\\"")
                            '\\' -> append("\\\\")
                            '\n' -> append("\\n")
                            '\r' -> append("\\r")
                            '\t' -> append("\\t")
                            else -> append(c)
                        }
                    append('"')
                }

    }
}
