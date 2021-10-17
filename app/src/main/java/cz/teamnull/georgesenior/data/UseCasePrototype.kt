package cz.teamnull.georgesenior.data

import kotlin.reflect.KFunction1

class UseCasePrototype(val parent: String?, val name: String, val forward: KFunction1<UseCase, Boolean>, vararg val phrases: String) {
    override fun toString() = "PARENT: $parent, NAME: $name, PHRASES: $phrases\n"
}