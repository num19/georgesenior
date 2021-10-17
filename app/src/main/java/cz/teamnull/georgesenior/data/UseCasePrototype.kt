package cz.teamnull.georgesenior.data

import kotlin.reflect.KFunction1

class UseCasePrototype(val name: String, val parent: String?, val forward: KFunction1<Set<String>, Boolean>, vararg val phrases: String) {
    override fun toString() = "PARENT: $parent, NAME: $name, PHRASES: $phrases\n"
}