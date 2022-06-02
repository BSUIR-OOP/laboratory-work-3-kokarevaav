package model.factory

import model.abstraction.Shape
import model.*
import kotlin.collections.HashMap

class ShapeFactory private constructor() {
    private val shapes = HashMap<String, (List<Double>) -> Shape>()

    companion object {
        private val instance = ShapeFactory()

        fun getInstance(): ShapeFactory {
            return instance
        }
    }

    init {
        shapes["Circle"] = { arr ->
            Circle(arr[0], arr[1], arr[2])
        }
        shapes["Ellipse"] = { arr ->
            Ellipse(arr[0], arr[1], arr[2], arr[3])
        }
        shapes["Triangle"] = { arr ->
            EquilateralTriangle(arr[0], arr[1], arr[2])
        }
        shapes["Line"] = { arr ->
            Line(arr[0], arr[1], arr[2], arr[3])
        }
        shapes["Rectangle"] = { arr ->
            Rectangle(arr[0], arr[1], arr[2], arr[3])
        }
        shapes["Square"] = { arr ->
            Square(arr[0], arr[1], arr[2])
        }
    }

    fun getNames(): MutableSet<String> {
        return shapes.keys
    }

    fun getShape(title: String, args: List<Double>): Shape {
        val result = shapes[title]
        if (result != null) {
            return result.invoke(args)
        }
        throw IllegalArgumentException()
    }
}