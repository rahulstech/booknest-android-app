package rahulstech.android.booknest.util

/** Formats an integer as Indian number system (e.g., 29996 → "29,996") */
fun Int.formatIndian(): String {
    val s = this.toString()
    if (s.length <= 3) return s
    val last3 = s.takeLast(3)
    val rest = s.dropLast(3)
    val groups = mutableListOf<String>()
    var i = rest.length
    while (i > 0) {
        val start = maxOf(0, i - 2)
        groups.add(0, rest.substring(start, i))
        i = start
    }
    return groups.joinToString(",") + "," + last3
}