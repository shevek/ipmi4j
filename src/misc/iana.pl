#!/usr/bin/perl

use strict;
use warnings;
use LWP;
use LWP::UserAgent;

# Which enterprise numbers to include.
my %include = map { $_ => 1 } (
	2,		# IBM
	11,		# Hewlett Packard
	343,	# Intel
	674,	# Dell
	4542,	# Alerting Specifications Forum
	4769,	# IBM
	7154,	# Required by section 22.24
	20301,	# IBM eServer X
);

my $ua = new LWP::UserAgent();
my $req = new HTTP::Request(GET => 'http://www.iana.org/assignments/enterprise-numbers/enterprise-numbers');
my $res = $ua->request($req);
die "Failed: $@" unless $res->is_success;

print <<"EOM";
package org.anarres.ipmi.protocol;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public enum IanaEnterpriseNumber {
EOM

sub escape {
	local $_ = shift;
	s/([\\"])/\\$1/g;
	return $_;
}

my $data = 0;
my @data = ();
my %data = ();	# I love Perl
for (split(/\n/, $res->content)) {
	$data = 1 if /^0/;
	next unless $data;
	# last if /^$/;	# First empty line in data is EOD.
	if (/^[a-zA-Z(]/) {
		last unless @data;
		$data[-1] .= " $_";	## Bug in dataset
		next;
	}
	s/^\s+//;
	s/\s+$//;
	if (/^$/) {
		next unless @data; # Bug in dataset
		next if @data >= 2 && $data[1] =~ /Alticast/;
	}
	push(@data, $_);
	# print scalar(@data), " -> @data\n";
	if (@data == 4) {
		my $name = $data[1];
		if (length $name) {
			$name =~ s/\s*\(previous was.*//;
			$name =~ s/[\s\.,_\/'"`(){}?&*+!=|\\@#;:-]+/_/g;
			$name =~ s/_$//g;
			$name =~ tr/\x20-\x7f//cd;
			$name = "_$name" if $name =~ /^\d/;
			$data[$_] = escape($data[$_]) for (1, 2, 3);
			if (exists $data{$name}) {
				$name .= $data{$name}++;
			} else {
				$data{$name} = 1;
			}
			if ($include{$data[0]}) {
				print "\t/** $data[1]. $data[2] <$data[3]> */\n";
				print "\t$name($data[0], \"$data[1]\"),\n";
			}
		}
		@data = ();
	}
}

print <<"EOM";
	EOD(-1, null);

	private final int number;
	private final String name;

	private IanaEnterpriseNumber(int number, String name) {
		this.number = number;
		this.name = name;
	}

	\@Nonnegative
	public int getNumber() {
		return number;
	}

	\@Nonnull
	public String getName() {
		return name;
	}
}
EOM
