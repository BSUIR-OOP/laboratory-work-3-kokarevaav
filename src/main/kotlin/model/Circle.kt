package model

import model.abstraction.SymmetricShape

data class Circle(
    override var x: Double,
    override var y: Double,
    var side: Double
): SymmetricShape(x, y, side)