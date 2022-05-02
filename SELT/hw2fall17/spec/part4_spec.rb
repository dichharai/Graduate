require 'part4.rb'

describe "Class" do
  it "should have an attr_accessor_with_history method" do
#    lambda { Class.new.attr_accessor_with_history :x }.should_not raise_error
    expect(lambda { Class.new.attr_accessor_with_history :x }).not_to raise_error
  end
end

