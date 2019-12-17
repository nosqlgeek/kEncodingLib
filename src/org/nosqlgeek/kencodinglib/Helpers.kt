package org.nosqlgeek.kencodinglib

import java.nio.ByteBuffer

val INT_LENGTH = 4

/**
 * Convert an integer to a byte array
 */
fun intToByteArr(int : Int) : ByteArray {

    var result = ByteArray(INT_LENGTH)
    var bb : ByteBuffer = ByteBuffer.allocate(INT_LENGTH)
    bb.putInt(int)
    result = bb.array()

    return result;
}

/**
 * Convert a byte array to integer
 */
fun byteArrToInt(bytes : ByteArray, from : Int) : Int {

    var bInt = getByteRange( bytes, from, INT_LENGTH)
    return ByteBuffer.wrap(bInt).getInt();
}

/**
 * Get a range of bytes from a byte array
 */
fun getByteRange(bytes : ByteArray, from : Int, offset : Int) : ByteArray {

    var result = ByteArray(offset)
    var j = 0

    for ( i in from .. from + offset - 1 ) {

        result[j] = bytes[i]
        j++
    }

    return result
}