package me.karlito.seax.levels

class LevelCalculate {
    fun calculateLevel(xp: Int): Pair<Int, Int> {
        if (xp < 0) {
            println("Err")
        }

        val levelRequirements = mapOf(
            1 to 0,
            2 to 40,
            3 to 65,
            4 to 80,
            5 to 145,
            6 to 170,
            7 to 220,
            8 to 270
            // Add more level requirements as needed
        )

        val currentLevel = levelRequirements.entries.lastOrNull { xp >= it.value }?.key ?: 1
        val nextLevel = currentLevel + 1
        val xpNeededForNextLevel = levelRequirements[nextLevel] ?: (currentLevel * 35)

        val remainingXp = xpNeededForNextLevel - xp


        return Pair(currentLevel, xpNeededForNextLevel)
    }
}