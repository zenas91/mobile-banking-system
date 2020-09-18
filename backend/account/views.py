from django.shortcuts import render

from django.contrib.auth.models import User
from rest_framework import viewsets, filters, generics
from account.serializers import *
from account.models import *

from django_filters import rest_framework as restfilters
from rest_framework.filters import SearchFilter, OrderingFilter 

from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from django.contrib.auth.hashers import make_password, check_password


class CustomAuthToken(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data,
                                           context={'request': request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token, created = Token.objects.get_or_create(user=user)
        return Response({
            'token': token.key,
            'id': user.pk,
            'email': user.email
        })


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer
    ordering_fields = ('username', 'email')
    hashed_pwd = make_password("plain_text")
    check_password("plain_text",hashed_pwd) 


class AddressViewSet(viewsets.ModelViewSet):
    queryset = Address.objects.all()
    serializer_class = AddressSerializer


class AccountViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer

class AccountSearchViewSet(viewsets.ModelViewSet):
    queryset = Account.objects.all()
    serializer_class = AccountSerializer
    filter_backends = (restfilters.DjangoFilterBackend, SearchFilter, OrderingFilter)
    filter_fields = '__all__'


class TransactionViewSet(viewsets.ModelViewSet):
    queryset = Transaction.objects.all()
    serializer_class = TransactionSerializer

class AddressSearchViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Address.objects.all()
    serializer_class = AddressSerializer
    filter_backends = (restfilters.DjangoFilterBackend, SearchFilter, OrderingFilter)
    filter_fields = '__all__'

class SearchViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Transaction.objects.all()
    serializer_class = TransactionSerializer
    filter_backends = (restfilters.DjangoFilterBackend, SearchFilter, OrderingFilter)
    filter_fields = '__all__'
    search_fields = ('ref', 'debit', 'credit')