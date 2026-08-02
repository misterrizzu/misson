package com.example.data.local

import com.example.data.local.entity.*

object InitialData {

    val defaultHabits = listOf(
        HabitEntity(title = "Fajr Prayer (Namaz)", category = "Namaz", reminderHour = 5, reminderMinute = 15, isReminderEnabled = true),
        HabitEntity(title = "Dhuhr Prayer (Namaz)", category = "Namaz", reminderHour = 13, reminderMinute = 30, isReminderEnabled = true),
        HabitEntity(title = "Asr Prayer (Namaz)", category = "Namaz", reminderHour = 16, reminderMinute = 45, isReminderEnabled = true),
        HabitEntity(title = "Maghrib Prayer (Namaz)", category = "Namaz", reminderHour = 19, reminderMinute = 15, isReminderEnabled = true),
        HabitEntity(title = "Isha Prayer (Namaz)", category = "Namaz", reminderHour = 20, reminderMinute = 45, isReminderEnabled = true),
        HabitEntity(title = "Upload Clips", category = "Content", reminderHour = 14, reminderMinute = 0, isReminderEnabled = true),
        HabitEntity(title = "Cold Outreach / Marketing", category = "Business", reminderHour = 11, reminderMinute = 0, isReminderEnabled = true),
        HabitEntity(title = "Review Analytics", category = "Business", reminderHour = 21, reminderMinute = 0, isReminderEnabled = true),
        HabitEntity(title = "30 Min Workout", category = "Health", reminderHour = 7, reminderMinute = 0, isReminderEnabled = true),
        HabitEntity(title = "Read 10 Pages", category = "Mindset", reminderHour = 22, reminderMinute = 0, isReminderEnabled = true),
        HabitEntity(title = "Sleep Before Midnight", category = "Health", reminderHour = 23, reminderMinute = 30, isReminderEnabled = true)
    )

    val milestones = listOf(
        MilestoneEntity(targetAmount = 10000.0, title = "₹10,000 Milestone"),
        MilestoneEntity(targetAmount = 25000.0, title = "₹25,000 Milestone"),
        MilestoneEntity(targetAmount = 50000.0, title = "₹50,000 Milestone"),
        MilestoneEntity(targetAmount = 100000.0, title = "₹1 Lakh Milestone"),
        MilestoneEntity(targetAmount = 250000.0, title = "₹2.5 Lakh Milestone"),
        MilestoneEntity(targetAmount = 500000.0, title = "₹5 Lakh Milestone"),
        MilestoneEntity(targetAmount = 1000000.0, title = "₹10 Lakh Ultimate Mission")
    )

    val visionItems = listOf(
        VisionItemEntity(title = "₹10 Lakh Goal", targetAmountString = "₹10,00,000", description = "Primary Mission: Financial freedom baseline.", category = "Finance"),
        VisionItemEntity(title = "₹1 Crore Goal", targetAmountString = "₹1,00,00,000", description = "Long-term wealth building phase.", category = "Finance"),
        VisionItemEntity(title = "Freedom", targetAmountString = "100% Ownership", description = "Complete control over time, work, and decisions.", category = "Lifestyle"),
        VisionItemEntity(title = "Parents", targetAmountString = "Comfort & Hajj", description = "Providing financial security and peace for parents.", category = "Family"),
        VisionItemEntity(title = "Dream Bike", targetAmountString = "Paid Cash", description = "Reward for reaching major milestone.", category = "Reward"),
        VisionItemEntity(title = "Dream Office", targetAmountString = "Minimalist Studio", description = "Custom clean setup built for deep focus.", category = "Work")
    )

    val achievements = listOf(
        AchievementEntity(code = "FIRST_100", title = "First Spark", description = "Earn your first ₹100"),
        AchievementEntity(code = "FIRST_1000", title = "Proof of Work", description = "Earn your first ₹1,000"),
        AchievementEntity(code = "FIRST_10000", title = "Five Figures", description = "Earn your first ₹10,000"),
        AchievementEntity(code = "STREAK_7", title = "Week of Steel", description = "Maintain a 7-day mission streak"),
        AchievementEntity(code = "STREAK_30", title = "Unstoppable Momentum", description = "Maintain a 30-day mission streak"),
        AchievementEntity(code = "CLIPS_100", title = "Content Engine", description = "Upload 100 total content clips"),
        AchievementEntity(code = "CLIPS_500", title = "Media Mogul", description = "Upload 500 total content clips"),
        AchievementEntity(code = "CLIPS_1000", title = "1,000 Clips Master", description = "Upload 1,000 total content clips")
    )

    val quotes = listOf(
        // Discipline
        MotivationalQuoteEntity(quote = "Discipline beats motivation.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Small actions repeated daily create massive success.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Focus on today's work.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Don't build more. Ship more.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Excuses are the enemy of execution.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Action removes anxiety. Do the work.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Self-discipline is freedom in disguise.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Do what you said you would do.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "No bad days, only character-building days.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Master your mornings, master your mission.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "The standard is excellence, every single day.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Pain of discipline or pain of regret: choose.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Your future self will thank you for today's effort.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Consistency is the ultimate competitive advantage.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Stop overthinking, start executing.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Suffer the boredom of repetition.", category = "Discipline"),
        MotivationalQuoteEntity(quote = "Hard choices, easy life. Easy choices, hard life.", category = "Discipline"),

        // Islamic
        MotivationalQuoteEntity(quote = "Tie your camel, then trust Allah.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Work as if everything depends on you, pray as if everything depends on Allah.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Allah loves when you do a job that you perfect it.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Halal income brings barakah that numbers cannot measure.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Patience and prayer are your greatest anchors.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Seek provision with dignity and gratitude.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Every honest effort is recorded and rewarded.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Trust the timing of your Creator.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Charity does not decrease wealth; it purifies it.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Maintain pure intentions in all your pursuits.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Gratitude unlocks abundance.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Your Rizq is already written; execute with honor.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Dua + Action = Unstoppable Force.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Be truthful in business, for honesty brings blessing.", category = "Islamic"),
        MotivationalQuoteEntity(quote = "Never lose hope in the mercy and power of Allah.", category = "Islamic"),

        // Business
        MotivationalQuoteEntity(quote = "Solve real problems, capture real value.", category = "Business"),
        MotivationalQuoteEntity(quote = "Revenue is the ultimate validation.", category = "Business"),
        MotivationalQuoteEntity(quote = "Build in silence, let success make the noise.", category = "Business"),
        MotivationalQuoteEntity(quote = "Distribution is king. Content is the vehicle.", category = "Business"),
        MotivationalQuoteEntity(quote = "Focus on the unit economics.", category = "Business"),
        MotivationalQuoteEntity(quote = "Speed of execution is your unfair advantage.", category = "Business"),
        MotivationalQuoteEntity(quote = "Scale what works, cut what doesn't immediately.", category = "Business"),
        MotivationalQuoteEntity(quote = "Audience attention is the new currency.", category = "Business"),
        MotivationalQuoteEntity(quote = "Turn every reject into a lesson for victory.", category = "Business"),
        MotivationalQuoteEntity(quote = "Process creates repeatable output.", category = "Business"),
        MotivationalQuoteEntity(quote = "One core offer, one core audience, relentless focus.", category = "Business"),
        MotivationalQuoteEntity(quote = "A CEO measures output, not just hours logged.", category = "Business"),
        MotivationalQuoteEntity(quote = "Feedback is gold; iterate without ego.", category = "Business"),
        MotivationalQuoteEntity(quote = "Keep overhead low and execution high.", category = "Business"),
        MotivationalQuoteEntity(quote = "Great products solve painful problems cleanly.", category = "Business"),
        MotivationalQuoteEntity(quote = "Systems scale, hustle burns out.", category = "Business"),

        // Money
        MotivationalQuoteEntity(quote = "₹10,00,000 is not luck; it's a math problem.", category = "Money"),
        MotivationalQuoteEntity(quote = "Track every rupee, respect every rupee.", category = "Money"),
        MotivationalQuoteEntity(quote = "Wealth is built by producing more than you consume.", category = "Money"),
        MotivationalQuoteEntity(quote = "Financial freedom buys time, control, and peace.", category = "Money"),
        MotivationalQuoteEntity(quote = "Focus on income generation, then compounding.", category = "Money"),
        MotivationalQuoteEntity(quote = "Cashflow is the oxygen of your mission.", category = "Money"),
        MotivationalQuoteEntity(quote = "Never spend money before you have earned it.", category = "Money"),
        MotivationalQuoteEntity(quote = "Asset over liabilities every single time.", category = "Money"),
        MotivationalQuoteEntity(quote = "Multiple distribution streams fuel single-minded goal.", category = "Money"),
        MotivationalQuoteEntity(quote = "Value creation leads directly to monetary reward.", category = "Money"),
        MotivationalQuoteEntity(quote = "₹25,000/mo is Level 1. Master it and move up.", category = "Money"),
        MotivationalQuoteEntity(quote = "Mindset determines your ceiling.", category = "Money"),
        MotivationalQuoteEntity(quote = "Invest in skills that generate high-margin cashflow.", category = "Money"),
        MotivationalQuoteEntity(quote = "Money follows impact and volume.", category = "Money"),

        // Focus
        MotivationalQuoteEntity(quote = "Say no to 99% of things to excel at the 1%.", category = "Focus"),
        MotivationalQuoteEntity(quote = "No new projects until Mission 10L is complete.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Single-tasking is the superpower of high achievers.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Distraction is the thief of lifetime goals.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Deep work compounds faster than cheap dopamine.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Protect your morning energy at all costs.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Clear desk, clear mind, clear execution.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Turn off notifications. Turn on productivity.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Focus is deciding what NOT to do.", category = "Focus"),
        MotivationalQuoteEntity(quote = "One goal, one target: ₹10,00,000.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Laser focus dissolves obstacles.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Do the hardest task first every day.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Silence the noise, multiply the signal.", category = "Focus"),
        MotivationalQuoteEntity(quote = "Clarity breeds confidence.", category = "Focus"),

        // Consistency
        MotivationalQuoteEntity(quote = "Show up even when you don't feel like it.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Day 1 or One Day: You decide.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Continuous effort unlocks potential.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Build the daily streak. Guard the streak.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Unremarkable daily steps create remarkable results.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Don't break the chain.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Habits are the compound interest of self-improvement.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Winners do what losers don't want to do daily.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Small daily wins accumulate into unstoppable power.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Execution today determines tomorrow's reality.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Stay relentless through the slow phases.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Patience during growth, speed during execution.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Trust the process, measure the outputs.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Every posted clip brings you closer to the target.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "What you do today decides whether ₹10,00,000 becomes reality.", category = "Consistency"),
        MotivationalQuoteEntity(quote = "Be so consistent they cannot ignore you.", category = "Consistency")
    )
}
