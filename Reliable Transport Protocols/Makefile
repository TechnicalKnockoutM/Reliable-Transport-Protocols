SRC_DIR = ./src
BINS = abt gbn sr

SRC_FILES = $(wildcard $(SRC_DIR)/*.c)

CC = gcc

all: $(BINS)

%: $(SRC_DIR)/%.c
	$(CC) -std=c99 -o $@ $<

clean:
	rm -f abt gbn sr
