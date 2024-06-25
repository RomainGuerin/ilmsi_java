package com.e3in.java.utils;

import com.e3in.java.model.User;

/**
 * Interface pour les contrôleurs qui doivent être conscients de l'utilisateur.
 * Implémente cette interface pour permettre de passer l'utilisateur actuel
 * aux contrôleurs concernés.
 */
public interface UserAwareController {

    /**
     * Définit l'utilisateur actuel pour le contrôleur.
     *
     * @param user L'utilisateur actuel.
     */
    void setUser(User user);
}
