package org.nosqlgeek.kencodinglib

val ENC_BYTES : Byte = 1
val ENC_INT : Byte = 2
val ENC_STRING : Byte = 3

open class Element(val data : ByteArray, val encoding : Byte)

class RawElement(val bData : ByteArray) : Element(bData, ENC_BYTES)
class StringElement(val strData : String) : Element(strData.toByteArray(), ENC_STRING)
class IntElement(val iData : Int) : Element(intToByteArr(iData), ENC_INT)