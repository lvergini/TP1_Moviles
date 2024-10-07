package com.definit.tp1patronobs

enum class Genders(val gender: String) {
    UNKNOWN("Select Gender"),
    MASCULINO("Masculino"),
    FEMENINO("Femenino"),
    OTRO("Otro");

    override fun toString(): String {
        return gender
    }


}