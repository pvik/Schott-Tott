package org.pvik.st.engine

object Helper {
    fun isSequence(l : List<Int>) : Boolean {

        for (i in 1 .. (l.size-1)) {
            if ((l[i-1] + 1) != l[i])
                return false
        }
        return true
    }
}