#!/usr/bin/perl

use strict;
use warnings;
use LWP;
use LWP::UserAgent;

my $ua = new LWP::UserAgent();
my $req = new HTTP::Request(GET => 'http://www.iana.org/assignments/enterprise-numbers/enterprise-numbers');
my $res = $ua->request($req);
die "Failed: $@" unless $res->is_success;

print <<"EOM";
package org.anarres.ipmi.protocol;

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
	last if /^$/;	# First empty line in data is EOD.
	s/^\s+//;
	s/\s+$//;
	if (/^da Faculdade/) {
		$data[-1] .= " $_";	## Bug in dataset
		next;
	}
	push(@data, $_);
	if (@data == 4) {
		my $name = $data[1];
		next unless length $name;
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
		print "\t/** $data[1]. $data[2] <$data[3]> */\n";
		print "\t$name((short) $data[0]),\n";
		@data = ();
	}
}

print <<"EOM";
	EOD((short) -1);

	private final short number;
	// private final String name;

	private IanaEnterpriseNumber(short number /*, String name */) {
		this.number = number;
		// this.name = name;
	}
}
EOM
