package model

import model.abstraction.Shape

data class Line(
    var x1: Double,
    var y1: Double,
    var x2: Double,
    var y2: Double
): Shape(x1, y1, x2, y2)