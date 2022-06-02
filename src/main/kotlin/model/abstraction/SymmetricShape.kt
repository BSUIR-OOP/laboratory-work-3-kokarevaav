package model.abstraction

abstract class SymmetricShape(
    override var x: Double,
    override var y: Double,
    side: Double
): Shape(x, y, side, side)