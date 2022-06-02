import model.abstraction.Shape
import model.factory.ShapeFactory
import serializer.ShapeSerializer
import service.FileService
import utils.getProperties
import utils.printElements
import utils.printFileTypes
import utils.printMenu

val fileService = FileService("SONTest.txt")
val coder = ShapeSerializer()
val shapeNames = ShapeFactory.getInstance().getNames()
val shapes = mutableListOf<Shape>()

enum class Action(val raw: String) {
    Add("1"),
    Edit("2"),
    Delete("3"),
    View("4"),
    Encode("5"),
    Decode("6")
}

fun main() {
    while (true) {
        printMenu()
        val option = chooseAction(readln())
        performAction(option)
    }
}

fun performAction(action: Action) {
    when (action) {
        Action.Add -> addElement()
        Action.Edit -> editElement()
        Action.Delete -> deleteElement()
        Action.View -> viewElements()
        Action.Decode -> decodeElements()
        Action.Encode -> encodeElements()
    }
}

fun addElement() {
    printFileTypes()
    addShape()
}
fun editElement() {
    printElements(shapes)
    println("Choose an element to edit:")
    val numEl = readln()
    shapes.removeAt(numEl.toInt())
    addShape()
}
fun deleteElement() {
    printElements(shapes)
    println("Choose an element to delete:")
    val numEl = readln()
    shapes.removeAt(numEl.toInt())
}
fun viewElements() {
    printElements(shapes)
}
fun decodeElements() {
    val sonString = fileService.loadFromFile()
    shapes.addAll(coder.deserialize(sonString))
}
fun encodeElements() {
    fileService.saveToFile(coder.serialize(shapes))
}

fun addShape() {
    println("Choose shape type: ")
    val shapeType = readln()
    val properties = getProperties(shapeType)
    val args = mutableListOf<Double>()
    try {
        properties.forEach { property ->
            println("Enter $property: ")
            val arg = readln()
            args.add(arg.toDouble())
        }
    } catch (_: Exception) {
        println("Error occurred! Try again")
        return
    }
    val shape = ShapeFactory.getInstance().getShape(shapeType, args)
    shapes.add(shape)
    println("Shape has been successfully added")
}

fun chooseAction(input: String): Action {
    return Action.values().firstOrNull { act -> act.raw == input }!!
}