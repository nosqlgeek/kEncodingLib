package org.nosqlgeek.kencodinglib

import java.nio.ByteBuffer

class ZipList(val size : Int) {

    //The inner byte array that holds the list
    val bytes = ByteArray(size)


    /**
     * Initialize the ZipList by setting some meta data
     *
     * A zip list has the following format
     *
     * <SIZE> <LAST> <ELEMENT> .. <ELEMENT> .. <END>
     */
    init {
        // <SIZE> : Add the size to the first 4 bytes
        var bSize = intToByteArr(size)
        setByteRange(bSize, 0)

        // <LAST> : Reference to the last element
        var bLast = intToByteArr(8)
        setByteRange(bLast, 4)

        // <END> : Mark the end of the list with a special byte
        bytes.set(bytes.size-1, Byte.MAX_VALUE);

        println("Stop")
    }


    /**
     * Convert an integer to a byte array
     */
    fun intToByteArr(int : Int) : ByteArray {

        val CAPACITY = 4

        var result = ByteArray(CAPACITY)
        var bb : ByteBuffer = ByteBuffer.allocate(CAPACITY)
        bb.putInt(int)
        result = bb.array()

        return result;
    }

    /**
     * Set some bytes
     */
    fun setByteRange( source : ByteArray, from : Int) {

        for ( i in 0 .. source.size -1 ) {

            bytes[from + i] = source[i]
        }

    }

    /**
     * Set some bytes
     */
    fun getByteRange( from : Int, offset : Int) : ByteArray {

        var result = ByteArray(offset)
        var j = 0

        for ( i in from .. from + offset - 1 ) {

            result[j] = bytes[i]
            j++
        }

        return result
    }


    /**
     * Get the allocated length of the ZipList
     */
    fun metaLength() : Int {

        var bSize = getByteRange(0, 4)

        return ByteBuffer.wrap(bSize).getInt();
    }
}