package cz.teamnull.georgesenior.data

import kotlin.reflect.KFunction1

class UseCase(val parent: String?, val name: String, val forward: KFunction1<UseCasePrototype, Unit>, val phrases: List<Set<String>>) {
    override fun toString() = "PARENT: $parent, NAME: $name, PHRASES: $phrases\n"
}