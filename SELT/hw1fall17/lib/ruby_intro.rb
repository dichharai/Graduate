# When done, submit this entire file to the ICON HW1 Dropbox.

# Part 1
def sum(array)
  return 0 if array.empty? 
  sum = 0
  array.each{ |num| sum += num }
  return sum
end

def max_2_sum(arr)
  return 0 if arr.empty? 
  sum = 0
  sorted_arr = arr.sort
  sorted_arr.last(2).each{|num| sum += num}
  return sum
end

def sum_to_n?(arr, n)
  return false if arr.empty? || arr.length == 1
  return !!arr.uniq.combination(2).detect{|a, b| a+b == n}
  
end

# Part 2

def hello(name)
  "Hello," + " " + name
end

def starts_with_consonant?(s)
  !!(/[b-z&&[^eiou]]/i =~ s[0])
end

def binary_multiple_of_4?(s)
  return false if s.empty? 
  !!(/\A([0-1]*)\z/ =~ s) ? s.to_i(16)%4 == 0 : false 
 
end

# Part 3

class BookInStock
  attr_accessor(:isbn, :price)
  def initialize(isbn, price)
    raise ArgumentError if isbn.empty? || price <= 0
    @isbn = isbn
    @price = price
  end
  def isbn
    @isbn
  end
  def isbn=(isbn)
    @isbn = isbn
  end
  def price=(price)
    @price = price
  end
  def price
    @price
  end
  def price_as_string
    "$"+"%.2f"%(@price)
  end
    
end
