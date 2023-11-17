package me.karlito.seax.levels

class LevelCalculate {
    fun calculateLevel(exp: Int): Int {
        // Define level-up thresholds
        val levelThresholds = listOf(0, 25, 40, 60, 90, 130, 200, 240, 300, 350, 410, 470, 530, 600, 655, 710)

        // Find the current level based on the exp
        var currentLevel = 1
        var remainingExp = exp

        for (threshold in levelThresholds) {
            if (remainingExp >= threshold) {
                currentLevel++
                remainingExp -= threshold
            } else {
                break
            }
        }

        return currentLevel
    }
}