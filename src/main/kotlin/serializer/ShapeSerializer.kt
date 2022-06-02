package serializer

import by.sasha.serializerproject.serializer.Serializer
import model.abstraction.Shape
import model.factory.ShapeFactory
import kotlin.reflect.full.memberProperties

/**
 * SON (Sasha Object Notation) Coder
 */
class ShapeSerializer: Serializer<Shape> {
    private val ids = listOf("type", "fields")
    private lateinit var fields: Collection<String>

    private var currentIndex = 0
    private var sonString = ""

    /**
     * Encodes [objects] into SON using kotlin-reflection
     * @see getArgs
     *
     * @return Substring without last symbol to exclude unnecessary comma
     */
    override fun serialize(objects: Collection<Shape>): String {
        var out = String()
        objects.forEach { shape ->
            out += "{type=${shape::class.simpleName},fields={${getArgs(shape)}}},\n"
        }
        return out.substring(0..out.length - 3)
    }

    override fun deserialize(input: String): Collection<Shape> {
        sonString = input
        val result = mutableListOf<Shape>()
        val objects = input.replace(" ", "").split("\n")
        objects.forEach { string ->
            try { result.add(getObject(string)) }
            catch (ex: Exception) { ex.printStackTrace() }
        }

        return result
    }

    /**
     *
     */
    private fun getObject(string: String): Shape {
        currentIndex = 0
        sonString = string
        val args = mutableListOf<Double>()
        val typeToken = readToken()
        try {
            while (true) {
                val arg = readToken().second.toDouble()
                args.add(arg)
            }
        } catch (_: Exception) {}
        return ShapeFactory.getInstance().getShape(typeToken.second, args)
    }

    private fun readToken(): Pair<String, String> {
        val currentLetter = sonString[currentIndex]
        if (!currentLetter.isLetterOrDigit() && (currentLetter != '.')) {
            currentIndex++
            return readToken()
        }
        var id = readWord()

        if (id == "type") {
            skipSymbols()
            val value = readWord()
            initFieldConstants(value)
            return id to value
        }
        else if (id == "fields") {
            skipWord()
            id = readWord()
            skipWord()
            val value = readWord()
            return id to value
        }
        else if (fields.contains(id)) {
            skipSymbols()
            val value = readWord()
            return id to value
        }

        if (!ids.contains(id) && !fields.contains(id)) {
            throw java.lang.IllegalArgumentException("Token not found $id")
        }

        throw IllegalArgumentException("Illegal SON format")
    }

    private fun initFieldConstants(objectType: String) {
        val obj = ShapeFactory.getInstance().getShape(objectType, listOf(0.0, 0.0, 0.0, 0.0))
        fields = obj::class.memberProperties.map { prop -> prop.name }
    }

    private fun readWord(): String {
        var currentLetter = sonString[currentIndex]
        var res = ""
        if (currentLetter.isLetterOrDigit() || currentLetter == '.') {
            currentIndex++
            res += currentLetter + readWord()
        }
        return res
    }

    /**
     * @return An index of input string, where first letter or digit was found.
     */
    private fun skipSymbols() {
        while (!sonString[currentIndex].isLetterOrDigit()) {
            currentIndex++
        }
    }

    /**
     * If in "fromIndex" position is located a word, this method will skip it
     * And return a beginning index of THE NEXT word.
     *
     * @return An index of input string, where the next word starts
     */
    private fun skipWord() {
        while (sonString[currentIndex].isLetterOrDigit()) {
            currentIndex++
        }
        return skipSymbols()
    }

    /**
     * Accepts Shape subclass and, using kotlin-reflection,
     * Generates string with declared fields
     *
     * @return String in "fieldName=fieldValue" format for chosen object
     */
    private fun getArgs(shape: Shape): String {
        var out = String()
        val javaClass = shape.javaClass
        javaClass.declaredFields.forEach { field ->
            field.isAccessible = true
            val fieldName = field.name
            val fieldValue = field.get(shape)
            out += "${fieldName}=${fieldValue},"
        }
        return out.substring(0..out.length - 2)
    }
}

/*
{type=Rectangle,fields={x=12,y=13,width=15,height=30}},
{type=Triangle,fields={x=5,y=1,width=35,height=35}},
{type=Circle,fields={x=50,y=13,width=100,height=100}},
{type=Ellipse,fields={x=20,y=30,width=100,height=50}}
 */
