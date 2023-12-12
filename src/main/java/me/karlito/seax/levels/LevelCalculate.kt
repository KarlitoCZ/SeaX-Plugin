package me.karlito.seax.levels

class LevelCalculate {
    companion object {
        val maxLvl = 20
        val maxXp = 1050
    }

    fun calculateLevel(xp: Int): Pair<Int, Int> {
        if (xp < 0) {
            println("Err 0 or less cannot be a value")
        }


        val levelRequirements = mapOf(
            1 to 0,
            2 to 40,
            3 to 65,
            4 to 80,
            5 to 145,
            6 to 170,
            7 to 220,
            8 to 270,
            9 to 270,
            10 to 330,
            11 to 390,
            12 to 430,
            13 to 490,
            14 to 550,
            15 to 620,
            16 to 700,
            17 to 790,
            18 to 870,
            19 to 940,
            maxLvl to maxXp
            // Add more level requirements as needed
        )

        val currentLevel = levelRequirements.entries.lastOrNull { xp >= it.value }?.key ?: 1
        val nextLevel = currentLevel + 1
        val xpNeededForNextLevel = levelRequirements[nextLevel] ?: (currentLevel * 35)

        val remainingXp = xpNeededForNextLevel - xp


        return Pair(currentLevel, xpNeededForNextLevel)
    }
}