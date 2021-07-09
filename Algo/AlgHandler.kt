import java.util.Stack

enum class CurrentStage
{
    INITIALISATION,
    REVERSE_GRAPH_DFS,
    PRIORITIZED_GRAPH_DFS,
    COMPLETED
}


class AlgHandler<T>(private var currentStage : CurrentStage = CurrentStage.INITIALISATION,
                    private var graph : GraphOriented<T> = GraphOriented(),
                    private var searchStack : Stack<T> = Stack(),
                    private var handeledNodes : HashSet<T> = HashSet(),
                    private var nodesOrder : List<T> = emptyList<T>(),
                    private var connectComponents : MutableList<MutableList<T>> = mutableListOf<MutableList<T>>())
{
    private class AlgState<T>(val currentStage : CurrentStage = CurrentStage.INITIALISATION,
                              val graph : GraphOriented<T> = GraphOriented(),
                              val searchStack : Stack<T> = Stack(),
                              val handeledNodes : HashSet<T> = HashSet(),
                              val nodesOrder : List<T> = emptyList<T>(),
                              var connectComponents : MutableList<MutableList<T>> = mutableListOf<MutableList<T>>())
    {   
    // TODO frontend getters
    }

    private fun createAlgState() : AlgState<T>
    {
        return AlgState<T>(currentStage, graph, searchStack, handeledNodes, nodesOrder, connectComponents)
    }
    
    private fun restoreAlgState(algState : AlgState<T>)
    {
        this.currentStage = algState.currentStage
        this.graph = algState.graph
        this.searchStack = algState.searchStack
        this.handeledNodes = algState.handeledNodes
        this.nodesOrder = algState.nodesOrder
        this.connectComponents = algState.connectComponents
    }

    fun connectComponentsToString() : String
    {
        var result = String()
        for (i in connectComponents)
        {
            var head = i.joinToString(separator = ", ", prefix = "[", postfix = "]")
            result += head + " "
        }
        return result
    }

    override fun toString() : String
    {
        var res = String()
        val currentStageStr = currentStage.toString()
        val graphStr = graph.toString()
        val searchStackStr = searchStack.toString()
        val handeledNodesStr = handeledNodes.toString()
        val nodesOrderStr = nodesOrder.toString()
        val connectComponentsStr = this.connectComponentsToString()

        res += "currentStage is $currentStageStr\n"
        res += "graph is $graphStr\n"
        res += "searchStack is $searchStackStr\n"
        res += "handeledNodes is $handeledNodesStr\n"
        res += "nodesOrder is $nodesOrderStr\n"
        res += "connectComponents is $connectComponentsStr\n"
        
        return res
    }

    fun doAlgStep()
    {
        println(toString())
        when (currentStage)
        {
            CurrentStage.INITIALISATION -> 
            {
                if (graph.nodes.size == 0)
                {
                    currentStage = CurrentStage.COMPLETED
                    return
                }
                nodesOrder = graph.nodes.toList()
                searchStack.add(nodesOrder[0])
                handeledNodes.clear()
                currentStage = CurrentStage.REVERSE_GRAPH_DFS
                graph = graph.inversed()
                nodesOrder = emptyList()
                connectComponents.add(mutableListOf<T>())
                return
            }

            CurrentStage.REVERSE_GRAPH_DFS -> 
            {
                if (searchStack.isNotEmpty())
                {
                    val currentNode = searchStack.peek()

                    if (!(currentNode in handeledNodes))
                    {
                        handeledNodes.add(currentNode)
                        if (graph.adjacencyMap[currentNode] == null)
                            return
                        
                        for (node in graph.adjacencyMap[currentNode]!!)
                        {
                            if (!(node in handeledNodes))
                            {
                                searchStack.push(node)
                            }
                        }
                    }

                    else
                    {
                        searchStack.pop()
                        if (!(currentNode in nodesOrder))
                            nodesOrder += currentNode
                    }
                }

                else // Local stop, but there is nodes left
                {
                    if (handeledNodes.size == graph.nodes.size) // Aka there is no not handeled nodes
                    {
                        currentStage = CurrentStage.PRIORITIZED_GRAPH_DFS
                        graph = graph.inversed()
                        handeledNodes.clear()
                        nodesOrder = nodesOrder.reversed()
                        searchStack.add(nodesOrder[0])
                        return
                    }
                    
                    for (i in graph.nodes)
                    {
                        if (!(i in handeledNodes) )
                        {
                            searchStack.push(i)
                            return
                        }
                    }
                    throw Exception("No unhandelend nodes found but nodes.size != handeledNodes.size")
                }
                return
            }

            CurrentStage.PRIORITIZED_GRAPH_DFS ->
            {
                if (searchStack.isNotEmpty())
                {
                    val currentNode = searchStack.peek()

                    if (!(currentNode in handeledNodes))
                    {
                        handeledNodes.add(currentNode)
                        if (graph.adjacencyMap[currentNode] == null)
                            return

                        for (node in graph.adjacencyMap[currentNode]!!)
                        {
                            if (!(node in handeledNodes))
                            {
                                searchStack.push(node)
                            }
                        }
                    }

                    else
                    {
                        searchStack.pop()
                        var tail = connectComponents.last()
                        connectComponents.removeAt(connectComponents.size -1)
                        if (!(currentNode in tail))
                            tail.add(currentNode)
                        connectComponents.add(tail)
                    }
                }

                else
                {
                    if (handeledNodes.size == graph.nodes.size) // Aka there is no not handeled nodes
                    {
                        currentStage = CurrentStage.COMPLETED
                        handeledNodes.clear()
                        return
                    }

                    for (i in nodesOrder)
                    {
                        if (!(i in handeledNodes))
                        {
                            searchStack.push(i)
                            connectComponents.add(mutableListOf<T>())
                            return
                        }
                    }
                    throw Exception("No unhandelend nodes found but nodes.size != handeledNodes.size")
                }
                return                    
            }

            CurrentStage.COMPLETED ->
            {
                return
            } 
            
        }
    }

    fun doAlgUntilCompleted()
    {   
        while (currentStage != CurrentStage.COMPLETED)
        {
            doAlgStep()
        }
    }
}

fun main()
{
    //---------------------------------------
    val scan = java.util.Scanner(System.`in`)
    var c = ""
    var d = ""
    var enteredList: MutableList<Pair<String, String>> = mutableListOf()

    while(scan.hasNext()){
        c = scan.nextLine()

        if(scan.hasNext()){
            d = scan.nextLine()

            enteredList.add(Pair(c, d))            
        }
        else
            break
    }
    //---------------------------------------------

    var theGraph = mockInput(enteredList)
    println("Graph")
    println("$theGraph")

    var algHandler = AlgHandler(graph = theGraph)

    algHandler.doAlgUntilCompleted()
    println("Print alg result")
    val res = algHandler.connectComponentsToString()
    println("$res")
}