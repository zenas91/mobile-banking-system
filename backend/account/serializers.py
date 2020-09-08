from django.contrib.auth.models import User
from rest_framework import serializers

from account.models import *


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'first_name', 'last_name','username', 'email', 'password', 'last_login', 'is_staff', 'is_active', 'is_superuser')


#fields = ('id' 'firstname', 'lastname', 'dob', 'username', 'email', 'password', 'created', 'modified', 'address', 'accounts', 'last_login')


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
