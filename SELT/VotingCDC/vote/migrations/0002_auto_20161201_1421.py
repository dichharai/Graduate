# -*- coding: utf-8 -*-
# Generated by Django 1.9 on 2016-12-01 20:21
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('vote', '0001_added candidates and election_info table'),
    ]

    operations = [
        migrations.AlterField(
            model_name='candidates',
            name='c_image',
            field=models.ImageField(upload_to=b''),
        ),
    ]
