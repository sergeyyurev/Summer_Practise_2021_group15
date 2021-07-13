class GraphOriented<T>()
{
    var adjacencyMap : HashMap<T, HashSet<T>> = HashMap()
    var nodes : HashSet<T> = HashSet<T>()

    private fun loader(adjacencyMapInp : HashMap<T, HashSet<T>> , nodesInp : HashSet<T> )
    {
        adjacencyMap = adjacencyMapInp
        nodes = nodesInp
    }


    fun addEdge(sourceVertex: T, destinationVertex: T)
    {
        nodes.add(sourceVertex)
        nodes.add(destinationVertex)

        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(sourceVertex)

        adjacencyMap
            .computeIfAbsent(destinationVertex) { HashSet() }
            .add(destinationVertex)

        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(destinationVertex)
    }

    fun removeEdge(sourceVertex: T, destinationVertex: T)
    {
        if (adjacencyMap.containsKey(sourceVertex))
        {
            if (adjacencyMap[sourceVertex]!!.contains(destinationVertex))
            {
                adjacencyMap[sourceVertex]!!.remove(destinationVertex)
            }
        }
    }

    fun removeNode(vertex : T)
    {
        nodes.remove(vertex)

        if (adjacencyMap.containsKey(vertex))
            adjacencyMap.remove(vertex)
        for (i in adjacencyMap.keys)
        {
            removeEdge(i, vertex)
        }
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

    fun toList() : List<Pair<T, T>>
    {
        var res : List<Pair<T, T>> = emptyList()
        for (i in adjacencyMap.keys)
        {
            for (j : T in adjacencyMap[i]!!)
                res += Pair(i, j)
        }
        return res
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys)
        {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()

    fun copy() : GraphOriented<T>
    {
        val graph = GraphOriented<T>()
        graph.loader(adjacencyMap, nodes) 
        return graph
    }
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

