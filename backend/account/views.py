from django.shortcuts import render

from django.contrib.auth.models import User
from rest_framework import viewsets, filters, generics
from account.serializers import *
from account.models import *

from django_filters import rest_framework as restfilters
from rest_framework.filters import SearchFilter, OrderingFilter


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer
    ordering_fields = ('username', 'email')


class AddressViewSet(viewsets.ModelViewSet):
    queryset = Address.objects.all()
    serializer_class = AddressSerializer


class AccountViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer


class TransactionViewSet(viewsets.ModelViewSet):
    queryset = Transaction.objects.all()
    serializer_class = TransactionSerializer
    filter_backends = (restfilters.DjangoFilterBackend, SearchFilter, OrderingFilter)
    filter_fields = ('debit', 'credit', 'trans_type')
    search_fields = ('ref', 'debit', 'credit')


class SearchViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Transaction.objects.all()
    serializer_class = TransactionSerializer
    filter_backends = (restfilters.DjangoFilterBackend, SearchFilter, OrderingFilter)
    filter_fields = '__all__'
    search_fields = ('ref', 'debit', 'credit')