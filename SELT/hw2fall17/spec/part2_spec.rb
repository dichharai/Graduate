require 'part2.rb'

describe "#rps_game_winner" do
  it "should be defined" do
#    lambda { rps_game_winner([["X", "P"], ["Y", "S"]]) }.should_not raise_error
    expect(lambda {rps_game_winner([["X", "P"], ["Y", "S"]])}).not_to raise_error
  end
  # this is given for free in the outline
  it "should raise WrongNumberOfPlayersError if there are not exactly two players" do
#    lambda { rps_game_winner([ ["Allen", "S"] ]) }.should raise_error, "No error raised for incorrect number of players"
    expect(lambda { rps_game_winner([ ["Allen", "S"] ]) }).to raise_error(WrongNumberOfPlayersError)
  end
end

describe "#rps_tournament_winner" do
  it "should be defined" do
#    lambda { rps_tournament_winner([["X", "P"], ["Y", "S"]]) }.should_not raise_error
    expect(lambda { rps_tournament_winner([["X", "P"], ["Y", "S"]]) }).not_to raise_error
  end
end
