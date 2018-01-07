package com.fisk.twig.ide

data class EditingModel(
        val id: String,
        val name: String
) {
    companion object {
        val NEW = "new"
        val NONE = "none"
        val LEGACY = "legacy"
    }
}