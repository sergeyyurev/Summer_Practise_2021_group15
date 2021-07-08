class GraphOriented<T> {
    val adjacencyMap : HashMap<T, HashSet<T>> = HashMap()
    var nodes : HashSet<T> = HashSet<T>()

    fun addEdge(sourceVertex: T, destinationVertex: T)
    {
        nodes.add(sourceVertex)
        nodes.add(destinationVertex)

        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(destinationVertex)
    }

    fun inversed() : GraphOriented<T>
    {
        var res : GraphOriented<T> = GraphOriented()
        for (i in adjacencyMap.keys)
        {
            for (j : T in adjacencyMap[i]!!)
                res.addEdge(j, i)
        }

        return res
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()
}

fun mockInput(inp : List<Pair<String, String>>) : GraphOriented<String>
{
    var res = GraphOriented<String>()
    for (i in inp)
    {
        res.addEdge(i.first, i.second)
    }
    return res
}

// fun main()
// {
//     var theGraph = mockInput( listOf(Pair("a","b"), Pair("a","c"), Pair("b","c"), Pair("c","d")) )
//     println("$theGraph")

//     val res = theGraph.adjacencyMap["a"]
//     println("theGraph.adjacencyMap[a] = $res")

// }

