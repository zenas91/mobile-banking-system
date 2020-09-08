# Generated by Django 3.1.1 on 2020-09-08 15:35

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Account',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('created', models.DateTimeField(auto_now_add=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('enabled', models.BooleanField(default=True)),
                ('iban', models.CharField(max_length=20, unique=True)),
                ('number', models.CharField(max_length=20, unique=True)),
                ('act_type', models.CharField(choices=[('SAVINGS', 'Savings'), ('CURRENT', 'Current')], default=('SAVINGS', 'Savings'), max_length=20)),
                ('balance', models.IntegerField(blank=True)),
                ('overdraft', models.BooleanField(default=False)),
                ('owner', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='account_owner', to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Transaction',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('created', models.DateTimeField(auto_now_add=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('enabled', models.BooleanField(default=True)),
                ('ref', models.CharField(max_length=20, unique=True)),
                ('trans_type', models.CharField(choices=[('TRANSFER', 'Transfer'), ('CREDIT', 'Credit'), ('DEBIT', 'Debit')], default=('TRANSFER', 'Transfer'), max_length=20)),
                ('amount', models.IntegerField()),
                ('balbeforedebit', models.IntegerField(blank=True)),
                ('balafterdebit', models.IntegerField(blank=True)),
                ('balbeforecredit', models.IntegerField(blank=True)),
                ('balaftercredit', models.IntegerField(blank=True)),
                ('remark', models.CharField(blank=True, max_length=150)),
                ('credit', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='credit', to='account.account')),
                ('debit', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='debit', to='account.account')),
            ],
        ),
        migrations.CreateModel(
            name='Address',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('created', models.DateTimeField(auto_now_add=True)),
                ('modified', models.DateTimeField(auto_now=True, null=True)),
                ('enabled', models.BooleanField(default=True)),
                ('street', models.CharField(max_length=100)),
                ('housenumber', models.IntegerField(blank=True)),
                ('postcode', models.IntegerField(blank=True)),
                ('city', models.CharField(max_length=100)),
                ('state', models.CharField(max_length=100)),
                ('country', models.CharField(max_length=100)),
                ('owner', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]
