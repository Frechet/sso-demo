package ru.intabia.authenticator;

import org.keycloak.common.util.Time;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmsOtpCredentialProvider implements CredentialProvider, CredentialInputValidator, CredentialInputUpdater, OnUserCache {
    public static final String SMS_OTP = "SMS_OTP";
    public static final String CACHE_KEY = SmsOtpCredentialProvider.class.getName() + "." + SMS_OTP;

    protected KeycloakSession session;

    public SmsOtpCredentialProvider(KeycloakSession session) {
        this.session = session;
    }

    public CredentialModel getSecret(RealmModel realm, UserModel user) {
        CredentialModel secret = null;
        if (user instanceof CachedUserModel) {
            CachedUserModel cached = (CachedUserModel)user;
            secret = (CredentialModel)cached.getCachedWith().get(CACHE_KEY);

        } else {
            List<CredentialModel> creds = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_OTP);
            if (!creds.isEmpty()) secret = creds.get(0);
        }
        return secret;
    }


    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (!SMS_OTP.equals(input.getType())) return false;
        if (!(input instanceof UserCredentialModel)) return false;
        UserCredentialModel credInput = (UserCredentialModel) input;
        List<CredentialModel> creds = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_OTP);
        if (creds.isEmpty()) {
            CredentialModel secret = new CredentialModel();
            secret.setType(SMS_OTP);
            secret.setValue(credInput.getValue());
            secret.setCreatedDate(Time.currentTimeMillis());
            session.userCredentialManager().createCredential(realm ,user, secret);
        } else {
            creds.get(0).setValue(credInput.getValue());
            session.userCredentialManager().updateCredential(realm, user, creds.get(0));
        }
        session.userCache().evict(realm, user);
        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        if (!SMS_OTP.equals(credentialType)) return;
        session.userCredentialManager().disableCredentialType(realm, user, credentialType);
        session.userCache().evict(realm, user);

    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        if (!session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_OTP).isEmpty()) {
            Set<String> set = new HashSet<>();
            set.add(SMS_OTP);
            return set;
        } else {
            return Collections.EMPTY_SET;
        }

    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return SMS_OTP.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if (!SMS_OTP.equals(credentialType)) return false;
        return getSecret(realm, user) != null;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!SMS_OTP.equals(input.getType())) return false;
        if (!(input instanceof UserCredentialModel)) return false;

        String secret = getSecret(realm, user).getValue();

        return secret != null && ((UserCredentialModel)input).getValue().equals(secret);
    }

    @Override
    public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
        List<CredentialModel> creds = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_OTP);
        if (!creds.isEmpty()) user.getCachedWith().put(CACHE_KEY, creds.get(0));
    }
}
