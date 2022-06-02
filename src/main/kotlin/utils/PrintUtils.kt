package utils

import model.abstraction.Shape
import model.factory.ShapeFactory
import kotlin.reflect.full.memberProperties

fun printElements(elements: List<Shape>) {
    elements.forEachIndexed() { indx, shape ->
        print("$indx - ${shape::class.simpleName}")
    }
}

fun printFileTypes() {
    println("""
        Choose file type:
        ${ShapeFactory.getInstance().getNames()}
    """.trimIndent())
}

fun getProperties(shapeType: String): List<String> {
    val mock = ShapeFactory.getInstance().getShape(shapeType, listOf(0.0,0.0,0.0,0.0))
    return mock::class.memberProperties.map { prop -> prop.name }
}

fun printMenu() {
    println("""
        Choose an option:
        1.Add Entity, 2.Edit Entity, 3.Delete Entity
        4.View Entities, 5.Encode in File, 6.Decode from File
    """.trimIndent())
}