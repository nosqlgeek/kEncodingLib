package org.nosqlgeek.kencodinglib

import java.nio.ByteBuffer

class ZipList(val size : Int) {

    val HEADER_SIZE_START = 0
    val HEADER_TAIL_START = 4
    val HEADER_END = 7
    val INT_LENGTH = 4
    val END_MARKER = Byte.MAX_VALUE


    //The inner byte array that holds the list
    var bytes = ByteArray(size)


    /**
     * Initialize the ZipList by setting some meta data
     *
     * A zip list has the following format
     *
     * <SIZE> <TAIL> <ELEMENT> .. <ELEMENT> .. <END>
     */
    init {
        // <SIZE> : Add the size to the first 4 bytes
        var bSize = intToByteArr(size)
        setByteRange(bSize, HEADER_SIZE_START)

        // <TAIL> : Reference to the last element
        var tail = intToByteArr(-1)
        setByteRange(tail, HEADER_TAIL_START)

        // <END> : Mark the end of the list with a special byte
        bytes.set(bytes.size-1, END_MARKER);
    }


    /***
     * An element has the following details <PREV_LENGTH> <LENGTH> <ENCODING> <DATA>
     *
     *     * PREV_LENGTH: The length of the previous element
     *     * LENGTH: The length of this element
     *     * ENCODING: 1 - Integer, 2 - Bytes, 3 - String
     *     * DATA: Sequence of bytes
     *
     * BTW: Redis is using some optimizations (e.g. not storing the length if the encoding is integer). I am skipping
     * this here as it is more about visualizing the concept.
     */
    fun rpush(element : Element) {

        //Read the tail
        var tail = byteArrToInt(this.bytes, HEADER_TAIL_START)

        //TODO: Check if element fits into the pre-allocated memory


        var prevLength = -1
        var length = -1

        //If the Ziplist is empty
        if (tail == -1) {

            //Set the tail to the beginning of the list
            tail = HEADER_END + 1

            //Element header
            prevLength = 0
            length = element.data.size


        } else {

            //Read the length of the current element
            prevLength = byteArrToInt(getByteRange(bytes, tail + INT_LENGTH, INT_LENGTH), 0)
            length = element.data.size

            //Calculate the new tail position based on the length of the previous item
            tail = 2 * INT_LENGTH + 1 + prevLength

        }

        //Set the bytes in the list
        setByteRange(intToByteArr(prevLength), tail)
        setByteRange(intToByteArr(length), tail + INT_LENGTH)
        setByte(element.encoding, tail + 2*INT_LENGTH)
        setByteRange(element.data, tail + 2*INT_LENGTH + 1)

        //Update the tail reference
        setByteRange(intToByteArr(tail), HEADER_TAIL_START)

    }


    /**
     * Return all
     */
    /*
    fun all() : List<Element> {

    }
    */



    /**
     * Set some bytes
     */
    private fun setByteRange( source : ByteArray, from : Int) {

        for ( i in 0 .. source.size -1 ) {

            bytes[from + i] = source[i]
        }

    }

    /**
     * Set a single byte
     */
    private fun setByte(b : Byte, idx : Int) {

        bytes[idx] = b
    }



    /**
     * Get the allocated length of the ZipList
     */
    fun metaLength() : Int {

        return byteArrToInt(this.bytes, 0)
    }
}