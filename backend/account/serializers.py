from django.contrib.auth.models import User
from rest_framework import serializers

from account.models import *
from django.contrib.auth.hashers import make_password
from rest_framework.authtoken.models import Token


class UserSerializer(serializers.HyperlinkedModelSerializer):
  def create(self, validated_data):
    user = User(
        first_name=validated_data['first_name'],
		last_name=validated_data['last_name'],
        username=validated_data['username'],
		email=validated_data['email']
    )
    user.set_password(validated_data['password'])
    user.save()
    return user

  class Meta:
    model = User
    fields = ('id', 'first_name', 'last_name','username', 'email', 'password', 'last_login', 'is_staff', 'is_active', 'is_superuser')
    #fields = ('url', 'username', 'password', 'email', 'groups')
    extra_kwargs = {'password': {'write_only': True}}



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
