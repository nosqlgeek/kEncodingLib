package org.nosqlgeek.kencodinglib

fun testInitZipList() {
    var zipList = ZipList(1000)

    println(zipList.metaLength())

}

fun testRop() {

    var zipList = ZipList(1000)
    var e1 = StringElement("hello")
    zipList.rpush(e1)

    var e2 = StringElement("again")
    zipList.rpush(e2)

}