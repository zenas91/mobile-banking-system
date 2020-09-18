from django.contrib.auth.models import User
from rest_framework import serializers

from account.models import *
from django.contrib.auth.hashers import make_password
from rest_framework.authtoken.models import Token


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'first_name', 'last_name','username', 'email', 'password', 'last_login', 'is_staff', 'is_active', 'is_superuser')
        #extra_kwargs = {'password': {'write_only': True, 'required': True}}

    for user in User.objects.all():
        Token.objects.get_or_create(user=user)
    def validate_password(self, value: str) -> str:
        return make_password(value)

    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        return user


class AddressSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Address
        fields = ('id', 'owner', 'street', 'housenumber', 'postcode', 'city', 'state', 'country', 'created', 'modified', 'enabled')


class AccountSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Account
        fields = ('id','iban', 'number', 'owner', 'act_type', 'balance','overdraft', 'created', 'modified', 'enabled')


class TransactionSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Transaction
        fields = ('ref', 'amount', 'debit', 'credit', 'balbeforedebit', 'balafterdebit', 'balbeforecredit', 'balaftercredit', 'created', 'modified', 'enabled')
