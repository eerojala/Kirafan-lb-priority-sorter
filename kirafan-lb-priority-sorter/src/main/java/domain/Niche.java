package domain;

/*
    enum Niche is a subjective variable to signify if the character fills a niche which the user's other characters
    cannot fill.

    For example an user might assign an important niche to character if it has  specific class/element combination that
    the user currently lacks, for example a wind knight.

    A not important niche might be for example if the player has an existing moon warrior but lacks a moon warrior who
    has two offensive skills.
*/

public enum Niche {
    IMPORTANT,
    NOT_IMPORTANT,
    NONE
}
