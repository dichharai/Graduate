require 'part1.rb'

describe "#palindrome?" do
  it "should be defined" do
 #   lambda { palindrome?("Testing") }.should_not raise_error
    expect(lambda {palindrome?("Testing")}).not_to raise_error
  end
end

describe "#count_words" do
  it "should be defined" do
#    lambda { count_words("Testing") }.should_not raise_error
    expect(lambda{count_words("Testing")}).not_to raise_error
  end
  it "should return a Hash" do
#    count_words("Testing").class.should == Hash
    expect(count_words("Testing")).to be_a_kind_of(Hash) 
  end
end
