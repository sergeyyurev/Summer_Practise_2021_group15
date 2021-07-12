package gui.utils

import java.io.File

class Reader {
    companion object {
        @JvmStatic
        fun readingFromFile(file: File): Pair<MutableSet<String>, MutableSet<Pair<String, String>>>{ // ожидается, что в файле 1 строка - одно название вершины (как и до этого) (четные строки - откуда, нечет - куда)
            val list = file.readLines() // получили массив String (каждая отдельная строка)
            var prev = ""

            val arrayOfVertexes: MutableSet<String> = mutableSetOf()
            val arrayOfEdges: MutableSet<Pair<String, String>> = mutableSetOf()

            for (i in list.indices) {
                if (list[i][0] == '_') { // отдельная вершина
                    prev = ""
                    val str = list[i].substring(1)
                    arrayOfVertexes.add(str)
                }

                if (list[i][0]== '?') { // начало ребра
                    prev = list[i].substring(1)
                }
                else { // конец ребра
                    if (prev != "") {
                        arrayOfEdges.add(Pair(prev, list[i]))
                        arrayOfVertexes.add(prev)
                        arrayOfVertexes.add(list[i])
                        prev = ""
                    }
                    else
                        continue
                }
            }

            return Pair(arrayOfVertexes, arrayOfEdges)
        }
    }
}